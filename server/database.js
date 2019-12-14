var MongoClient = require('mongodb').MongoClient,
    ObjectID = require('mongodb').ObjectID;

var dbURL = "mongodb://localhost:27017";

var dbName = "autolock";

var observablesCollection = 'observables';
var devicesCollection     = 'devices';

exports.getObservables = function(){
    return new Promise(function(resolve, reject) {
        try{
            MongoClient.connect(dbURL)
            .then(function(client) {
                var db = client.db(dbName);
                var collection = db.collection(observablesCollection);
                collection.find().toArray(function(err, observables){
                    if(!err)
                        resolve(observables);
                    else
                        reject(err);
                });

            })
            .catch(function(err){
                reject(err);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.setObservableLocked = function(id, isLocked){
    return new Promise(function(resolve, reject) {
        try{
            MongoClient.connect(dbURL)
            .then(function(client) {
                var db = client.db(dbName);
                var collection = db.collection(observablesCollection);

                collection.updateOne(
                    {id: parseInt(id)},
                    {'$set': {locked: isLocked}}, 
                    function(err){
                        if(!err){
                            resolve();
                        }
                        else{
                            reject(err);
                        }
                });
            })
            .catch(function(err){
                reject(err);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.setObservableClosed = function(id, isClosed){
    return new Promise(function(resolve, reject) {
        try{
            MongoClient.connect(dbURL)
            .then(function(client) {
                var db = client.db(dbName);
                var collection = db.collection(observablesCollection);

                collection.updateOne({id: parseInt(id)},
                    {'$set': {closed: isClosed}}, function(err){
                        if(!err){
                            resolve();
                        }
                        else{
                            reject(err);
                        }
                });
            })
            .catch(function(err){
                reject(err);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.addDevice = function(firebase_token){
    return new Promise(function(resolve, reject) {
        try{
            MongoClient.connect(dbURL)
            .then(function(client) {

                var db = client.db(dbName);
                var collection = db.collection(devicesCollection);
                
                collection.insertOne({firebase_token: firebase_token}, function(err, result) {
                    if(err) { console.log("Here 3"); reject(err); }
                    else { resolve(result); }
                });

            })
            .catch(function(err){
                console.log("Here 2: " + err);
                reject(err);
            });
        }
        catch(err){
            console.log("Here 1");
            reject(err);
        }
    });
}

exports.getDevices = function(){
    return new Promise(function(resolve, reject) {
        try{
            MongoClient.connect(dbURL)
            .then(function(client) {
                var db = client.db(dbName);
                var collection = db.collection(devicesCollection);
                collection.find().toArray(function(err, devices){
                    if(!err)
                        resolve(devices);
                    else
                        reject(err);
                });

            })
            .catch(function(err){
                reject(err);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

