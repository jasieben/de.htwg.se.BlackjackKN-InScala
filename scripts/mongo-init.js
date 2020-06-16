db = db.getSiblingDB('playermanagement');
db.createUser(
    {
        user: 'root',
        pwd: '123',
        roles: [{ role: 'readWrite', db: 'playermanagement' }],
    },
);
db.createCollection('players');

db = db.getSiblingDB('gamelogic');
db.createUser(
    {
        user: 'root',
        pwd: '123',
        roles: [{ role: 'readWrite', db: 'gamelogic' }],
    },
);
db.createCollection('sessions');