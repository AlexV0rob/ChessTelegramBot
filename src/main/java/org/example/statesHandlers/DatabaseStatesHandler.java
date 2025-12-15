package org.example.statesHandlers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.chess.PositionOnBoard;
import org.example.states.LobbyState;
import org.example.states.LobbyState.LobbyType;
import org.example.states.UserState;
import org.example.states.UserState.MessengerType;
import org.example.states.UserState.UserStatus;

/**
 * Хранитель состояний в базе данных
 */
public class DatabaseStatesHandler implements StatesHandler {
    /**
     * Строка с ссылкой на базу данных
     */
    private final String url;

    /**
     * Конструктор
     */
    public DatabaseStatesHandler(String databaseURL) throws DatabaseException {
        url = databaseURL;
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();) {
            String usersDB = """
                    		CREATE TABLE IF NOT EXISTS users (
                    			prime_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    			unknown_id BIGINT,
                    telegram_id BIGINT,
                    discord_id BIGINT,
                    			status TINYINT NOT NULL,
                    			figure CHAR(1),
                    			start CHAR(2),
                    			finish CHAR(2),
                    			parts_count TINYINT,
                    			messenger TINYINT,
                    			lobby_name VARCHAR(16),
                    			lobby_id INTEGER,
                    			message_id BIGINT NOT NULL,
                    			games_played BIGINT NOT NULL,
                    			games_won BIGINT NOT NULL,
                    			player_name VARCHAR(16) NOT NULL
                    		)
                    """;

            String gamesDB = """
                    CREATE TABLE IF NOT EXISTS games (
                    	prime_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    	name VARCHAR(16) NOT NULL,
                    	first_user_id BIGINT NOT NULL,
                    	second_user_id BIGINT NOT NULL,
                    	type TINYINT NOT NULL,
                    	first_to_move BIT NOT NULL,
                    	chessboard BLOB NOT NULL,
                    	chessboard_side_length INTEGER NOT NULL,
                    	white_to_move BIT NOT NULL
                    )
                    """;

            String namesDB = """
                    CREATE TABLE IF NOT EXISTS names (
                    	prime_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    	name VARCHAR(16) NOT NULL,
                    	creator_id BIGINT NOT NULL
                    )
                    """;

            statement.execute(usersDB);
            statement.execute(gamesDB);
            statement.execute(namesDB);
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't connect to database", e);
        }
    }

    @Override
    public void setNewUserStatus(long userId, UserStatus status) {
        String updateQuery = """
                UPDATE users 
                SET status = ?
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            preparedStatement.setByte(1, getUserStatusCode(status));
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public String getUserLobbyName(long userId) {
        String selectQuery = """
                SELECT lobby_name 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getString(1) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void resetUserLobbyName(long userId) {
        String updateQuery = """
                UPDATE users 
                SET lobby_name = "" 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public void createNewLobby(String lobbyName, long firstPlayerId, long secondPlayerId, 
    		boolean isFirstPlayerWhite, LobbyType lobbyType, 
    		byte[][] chessboard, int sideLength, boolean isWhiteToMove) {
        String insertQuery = """
                INSERT INTO games 
                (name, first_user_id, second_user_id, type, first_to_move, 
                chessboard, chessboard_side_length, white_to_move)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            preparedStatement.setString(1, lobbyName);
            preparedStatement.setLong(2, firstPlayerId);
            preparedStatement.setLong(3, secondPlayerId);
            preparedStatement.setByte(4, getLobbyTypeCode(lobbyType));
            preparedStatement.setBoolean(5, isFirstPlayerWhite);
            preparedStatement.setBytes(6, getBoardBytesFromArray(chessboard, sideLength));
            preparedStatement.setInt(7, sideLength);
            preparedStatement.setBoolean(8, isWhiteToMove);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public LobbyType getLobbyType(String lobbyName) {
        String selectQuery = """
                SELECT type 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? getLobbyTypeByCode(result.getByte(1)) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteLobby(String lobbyName) {
        String deleteQuery = """
                DELETE FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);) {
            preparedStatement.setString(1, lobbyName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public long getLobbyAnotherUserId(String lobbyName, long userId) {
        String selectQueryFirst = """
                SELECT first_user_id 
                FROM games 
                WHERE name = ?
                """;
        String selectQuerySecond = """
                SELECT second_user_id 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatementFirst = connection.prepareStatement(selectQueryFirst);
             PreparedStatement preparedStatementSecond = connection.prepareStatement(selectQuerySecond);) {
            preparedStatementFirst.setString(1, lobbyName);
            preparedStatementSecond.setString(1, lobbyName);
            ResultSet resultFirst = preparedStatementFirst.executeQuery();
            ResultSet resultSecond = preparedStatementSecond.executeQuery();
            if (resultFirst.next() && resultSecond.next()) {
                long firstId = resultFirst.getLong(1);
                long secondId = resultSecond.getLong(1);
                return (userId == firstId) ? secondId : firstId;
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return -01;
    }

    @Override
    public void bookLobbyName(String lobbyName, long creatorId) {
        String insertQuery = """
                INSERT INTO names 
                (name, creator_id)
                VALUES (?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            preparedStatement.setString(1, lobbyName);
            preparedStatement.setLong(2, creatorId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public void unbookLobbyName(String lobbyName) {
        String deleteQuery = """
                DELETE FROM names 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);) {
            preparedStatement.setString(1, lobbyName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public long getLobbyCreator(String lobbyName) {
        String selectQuery = """
                SELECT creator_id 
                FROM names 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setUserLobbyName(long userId, String lobbyName) {
        String updateQuery = """
                UPDATE users 
                SET lobby_name = ?
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            preparedStatement.setString(1, lobbyName);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLobbyExisting(String lobbyName) {
        String selectQueryNames = """
                SELECT * 
                FROM names 
                WHERE name = ?
                """;
        String selectQueryGames = """
                SELECT * 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatementNames = connection.prepareStatement(selectQueryNames);
             PreparedStatement preparedStatementGames = connection.prepareStatement(selectQueryGames);) {
            preparedStatementNames.setString(1, lobbyName);
            preparedStatementGames.setString(1, lobbyName);
            ResultSet resultNames = preparedStatementNames.executeQuery();
            ResultSet resultGames = preparedStatementGames.executeQuery();
            return resultNames.next() || resultGames.next();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isLobbyAvailable(String lobbyName) {
        String selectQuery = """
                SELECT * 
                FROM names 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addUserNewMovePart(long userId, String movePart) {
        String selectQuery = """
                SELECT parts_count 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                byte partsCount = result.getByte(1);
                String updateQuery = switch (partsCount) {
                    case 0 -> """
                            UPDATE users 
                            SET figure = ?, parts_count = 1
                            WHERE prime_id = ?
                            """;
                    case 1 -> """
                            UPDATE users 
                            SET start = ?, parts_count = 2
                            WHERE prime_id = ?
                            """;
                    case 2 -> """
                            UPDATE users 
                            SET finish = ?, parts_count = 3
                            WHERE prime_id = ?
                            """;
                    case 3 -> """
                            UPDATE users 
                            SET figure = "", start = "", finish = "", parts_count = 0 
                            WHERE prime_id = ?
                            """;
                    default -> "";
                };
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateQuery);
                preparedStatementUpdate.setString(1, movePart);
                preparedStatementUpdate.setLong(2, userId);
                preparedStatementUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public String getUserMovePartFigure(long userId) {
        String selectQuery = """
                SELECT figure 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getString(1) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getUserMovePartStart(long userId) {
        String selectQuery = """
                SELECT start 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getString(1) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getUserMovePartFinish(long userId) {
        String selectQuery = """
                SELECT finish 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getString(1) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean isUserMoveReady(long userId) {
        String selectQuery = """
                SELECT parts_count 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getByte(1) == 3 : false;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isGameWhiteToMove(String lobbyName) {
        String selectQuery = """
                SELECT white_to_move 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getBoolean(1) : false;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public byte[][] getGameChessboard(String lobbyName) {
        String selectQuery = """
                SELECT chessboard, chessboard_side_length 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? 
            		getBoardArrayFromString(result.getBytes(1), result.getInt(2)) : 
            		null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeLobbyMovingUser(String lobbyName) {
        String selectQuery = """
                SELECT first_to_move 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                boolean movingSide = result.getBoolean(1);
                String updateQuery = """
                        UPDATE games 
                        SET first_to_move = ?
                        WHERE name = ?
                        """;
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateQuery);
                preparedStatementUpdate.setBoolean(1, !movingSide);
                preparedStatementUpdate.setString(2, lobbyName);
                preparedStatementUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public void moveGameFigure(String lobbyName, PositionOnBoard startPosition, PositionOnBoard finishPosition) {
        String selectQuery = """
                SELECT chessboard, chessboard_side_length 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                byte[][] chessboard = getBoardArrayFromString(result.getBytes(1), result.getInt(2));
                chessboard[finishPosition.row()][finishPosition.column()] =
                        chessboard[startPosition.row()][startPosition.column()];
                chessboard[startPosition.row()][startPosition.column()] = 0;
                String updateQuery = """
                        UPDATE games 
                        SET chessboard = ?
                        WHERE name = ?
                        """;
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateQuery);
                preparedStatementUpdate.setBytes(1, getBoardBytesFromArray(chessboard, result.getInt(2)));
                preparedStatementUpdate.setString(2, lobbyName);
                preparedStatementUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public void changeGameMovingSide(String lobbyName) {
        String selectQuery = """
                SELECT white_to_move 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                boolean movingSide = result.getBoolean(1);
                String updateQuery = """
                        UPDATE games 
                        SET white_to_move = ?
                        WHERE name = ?
                        """;
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateQuery);
                preparedStatementUpdate.setBoolean(1, !movingSide);
                preparedStatementUpdate.setString(2, lobbyName);
                preparedStatementUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public long addNewUser(MessengerType newUserMessenger, String userName) {
        String insertQuery = """
                INSERT INTO users 
                (unknown_id, telegram_id, discord_id, status, figure, start, 
                	finish, parts_count, messenger, lobby_name, lobby_id, message_id,games_played,games_won,user_name)
                VALUES (0, 0, 0, 0, "", "", "", 0, ?, "", -1, -1, 0, 0, ?)
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement =
                     connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setByte(1, getUserMessengerCode(newUserMessenger));
            preparedStatement.setString(2, userName);
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            return result.getLong(1);
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public UserStatus getUserStatus(long userId) {
        String selectQuery = """
                SELECT status 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? getUserStatusByCode(result.getByte(1)) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ImmutablePair<String, Double>> getBookedLobbies(long userId) {
    	//TODO
    	return null;
    	/*
        String selectQuery = """
                SELECT name 
                FROM names 
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            ResultSet result = preparedStatement.executeQuery();
            List<String> lobbiesNames = new ArrayList<String>();
            while (result.next()) {
                lobbiesNames.add(result.getString(1));
            }
            return lobbiesNames;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
        */
    }

    @Override
    public long getLobbyFirstPlayer(String lobbyName) {
        String selectQuery = """
                SELECT first_user_id 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : -1;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getLobbySecondPlayer(String lobbyName) {
        String selectQuery = """
                SELECT second_user_id 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : -1;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean isLobbyFirstPlayerToMove(String lobbyName) {
        String selectQuery = """
                SELECT first_to_move 
                FROM games 
                WHERE name = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setString(1, lobbyName);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getBoolean(1) : false;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void changeUserLastMessage(long userId, long lastMessageId) {
        String updateQuery = """
                UPDATE users 
                SET message_id = ? 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            preparedStatement.setLong(1, lastMessageId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't reset user move state");
            e.printStackTrace();
        }
    }

    @Override
    public long getUserMessageId(long userId) {
        String selectQuery = """
                SELECT message_id 
                FROM users  
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : -1;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void resetUserMoveState(long userId) {
        String updateQuery = """
                UPDATE users 
                SET figure = "", start = "", finish = "", parts_count = 0 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public MessengerType getUserMessenger(long userId) {
        String selectQuery = """
                SELECT messenger 
                FROM users 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? getUserMessengerByCode(result.getByte(1)) : null;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getUserMessengerId(long userId, MessengerType userMessenger) {
        String messengerField = switch (userMessenger) {
            case UserState.MessengerType.TELEGRAM -> "telegram_id";
            case UserState.MessengerType.DISCORD -> "discord_id";
        };
        String selectQuery = """
                SELECT %s 
                FROM users 
                WHERE prime_id = ?
                """.formatted(messengerField);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getUserIdFromTelegramId(long chatId) {
        String selectQuery = """
                SELECT prime_id 
                FROM users 
                WHERE telegram_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, chatId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getUserIdFromDiscordId(long chatId) {
        String selectQuery = """
                SELECT prime_id 
                FROM users 
                WHERE discord_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, chatId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getLong(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void addNewMessengerId(long userId, MessengerType newUserMessenger, long chatId) {
        String messengerField = switch (newUserMessenger) {
            case UserState.MessengerType.TELEGRAM -> "telegram_id";
            case UserState.MessengerType.DISCORD -> "discord_id";
        };
        String selectQuery = """
                UPDATE users 
                SET %s = ?
                WHERE prime_id = ?
                """.formatted(messengerField);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, chatId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isMessengerIdExisting(MessengerType messenger, long chatId) {
        String messengerField = switch (messenger) {
            case UserState.MessengerType.TELEGRAM -> "telegram_id";
            case UserState.MessengerType.DISCORD -> "discord_id";
        };
        String selectQuery = """
                SELECT prime_id FROM users 
                WHERE %s = ?
                """.formatted(messengerField);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, chatId);
            ResultSet result = preparedStatement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public ImmutablePair<String, Long> getUserStatistic(long chatId) {
        long userStatistic = 0;

        long playedGames = 0;
        long wonGames = 0;
        ResultSet result = getUserPlayedAndWonGames(chatId);
        try {
            playedGames = result.getLong(1);
            wonGames = result.getLong(2);
        } catch (SQLException e) {

            System.out.println("Error with database");
            e.printStackTrace();
        }
        if (playedGames != 0) {
            userStatistic = wonGames / playedGames;
        }
        return new ImmutablePair(getPlayerName(chatId), userStatistic);
    }

    @Override
    public void updateUserPlayedGames(long chatId) {
        long playedGames = 0;
        ResultSet result = getUserPlayedAndWonGames(chatId);

        String updateQuery = """
                UPDATE users 
                SET games_played = ? 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            playedGames = result.getLong(1);
            preparedStatement.setLong(1, playedGames);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserWonGames(long chatId) {
        long wonGames = 0;
        ResultSet result = getUserPlayedAndWonGames(chatId);
        String updateQuery = """
                UPDATE users 
                SET games_played = ? 
                WHERE prime_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            wonGames = result.getLong(2) + 1;
            preparedStatement.setLong(1, wonGames);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
    }

    /**
     * Получить состояние пользователя по его коду
     */
    private UserState.UserStatus getUserStatusByCode(byte code) {
        return switch (code) {
            case 0 -> UserState.UserStatus.MAINMENU;
            case 1 -> UserState.UserStatus.INGAME;
            case 2 -> UserState.UserStatus.CREATING;
            case 3 -> UserState.UserStatus.CHOOSING;
            case 4 -> UserState.UserStatus.AWAITING;
            case 5 -> UserState.UserStatus.MESSENGER_CHOOSING;
            default -> UserState.UserStatus.MAINMENU;
        };
    }

    /**
     * Получить код состояния пользователя
     */
    private byte getUserStatusCode(UserState.UserStatus status) {
        return switch (status) {
            case UserState.UserStatus.MAINMENU -> 0;
            case UserState.UserStatus.INGAME -> 1;
            case UserState.UserStatus.CREATING -> 2;
            case UserState.UserStatus.CHOOSING -> 3;
            case UserState.UserStatus.AWAITING -> 4;
            case UserState.UserStatus.MESSENGER_CHOOSING -> 5;
        };
    }

    /**
     * Получить тип матча по его коду
     */
    private LobbyState.LobbyType getLobbyTypeByCode(byte code) {
        return switch (code) {
            case 0 -> LobbyState.LobbyType.SINGLEPLAYER;
            case 1 -> LobbyState.LobbyType.MULTIPLAYER;
            default -> LobbyState.LobbyType.SINGLEPLAYER;
        };
    }

    /**
     * Получить код типа матча
     */
    private byte getLobbyTypeCode(LobbyState.LobbyType type) {
        return switch (type) {
            case LobbyState.LobbyType.SINGLEPLAYER -> 0;
            case LobbyState.LobbyType.MULTIPLAYER -> 1;
        };
    }

    /**
     * Получить мессенджер пользователя по его коду
     */
    private UserState.MessengerType getUserMessengerByCode(byte code) {
        return switch (code) {
            case 0 -> UserState.MessengerType.TELEGRAM;
            case 1 -> UserState.MessengerType.DISCORD;
            default -> null;
        };
    }

    /**
     * Получить код мессенджера пользователя
     */
    private byte getUserMessengerCode(UserState.MessengerType messenger) {
        return switch (messenger) {
            case UserState.MessengerType.TELEGRAM -> 0;
            case UserState.MessengerType.DISCORD -> 1;
        };
    }

    /**
     * Получить доску в виде массива из строки
     */
    private byte[][] getBoardArrayFromString(byte[] boardBytes, int sideLength) {
        byte[][] board = new byte[sideLength][sideLength];
        for (int i = 0; i < sideLength; ++i) {
            for (int j = 0; j < sideLength; ++j) {
                board[i][j] = boardBytes[i * sideLength + j];
            }
        }
        return board;
    }

    /**
     * Получить количество сыгранных игр
     */
    private ResultSet getUserPlayedAndWonGames(long chatId) {

        String selectQuery = """
                SELECT games_played, games_won 
                FROM users 
                WHERE primary_id = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, chatId);
            ResultSet result = preparedStatement.executeQuery();
            preparedStatement.close();
            connection.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получить имя пользователя
     */
    private String getPlayerName(long chatId) {
        String selectQuery = """
                SELECT player_name  
                FROM users 
                WHERE primary_id = ? 
                """;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {
            preparedStatement.setLong(1, chatId);

            ResultSet result = preparedStatement.executeQuery();
            String userName = result.next() ? result.getString(1) : "";
            preparedStatement.close();
            connection.close();
            return userName;
        } catch (SQLException e) {
            System.out.println("Error with database");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Получить доску в виде строки из массива
     */
    private byte[] getBoardBytesFromArray(byte[][] boardArray, int sideLength) {
        byte[] boardSymbols = new byte[sideLength * sideLength];
        for (int i = 0; i < sideLength; ++i) {
            for (int j = 0; j < sideLength; ++j) {
                boardSymbols[i * sideLength + j] = boardArray[i][j];
            }
        }
        return boardSymbols;
    }
}
