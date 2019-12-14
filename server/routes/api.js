module.exports = function(){
	var router = require('express').Router();
	var firebase = require("firebase-admin");
	
	var db = require('../database.js');
	var serviceAccount = require("../key.json");

	function setLocked(id, isLocked){
		return new Promise(function(resolve, reject) {
			db.setObservableLocked(id, isLocked)
			.then(function(){
				db.getDevices()
				.then(function(devices){

					var type = "unlocked";

					if(isLocked){
						type = "locked";
					}

					var payload = {
						data: {
							type: type,
							observable_id: id
						}
					};

					var options = {
						priority: "high",
						timeToLive: 60 * 60 *24
					};

					for(var i = 0; i < devices.length; i++){
						firebase.messaging().sendToDevice(devices[i].firebase_token, payload, options)
						.then(function(response) {})
						.catch(function(error) {});
					}

					resolve();
				})
				.catch(function(err){
					reject(err);
				});
			})
			.catch(function(err){
				reject(err);
			})
		});
	}

	function setClosed(id, isClosed){
		return new Promise(function(resolve, reject) {
			db.setObservableClosed(id, isClosed)
			.then(function(){
				db.getDevices()
				.then(function(devices){

					var type = "open";

					if(isClosed){
						type = "closed";
					}

					var payload = {
						data: {
							type: type,
							observable_id: id
						}
					};

					var options = {
						priority: "high",
						timeToLive: 60 * 60 *24
					};

					for(var i = 0; i < devices.length; i++){
						firebase.messaging().sendToDevice(devices[i].firebase_token, payload, options)
						.then(function(response) {})
						.catch(function(error) {});
					}

					resolve();
				})
				.catch(function(err){
					reject(err);
				});
			})
			.catch(function(err){
				reject(err);
			})
		});
	}

	firebase.initializeApp({
		credential: firebase.credential.cert(serviceAccount),
		databaseURL: "https://autolock-cee00.firebaseio.com"
	});
	
	router.get('/observables', function(req, res){

		db.getObservables()
		.then(function(observables){
			return res.json(observables);
		})
		.catch(function(error){
			return res.json(error);
		});
	});
	
	router.get('/:id/lock', function(req, res){
		var id = req.params.id;

		//TODO: Lock the door with id here

		setLocked(id, true)
		.then(function(){
			return res.json({status: 0});
		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		})
	});

	router.get('/:id/unlock', function(req, res){
		var id = req.params.id;

		//TODO: Unlock the door with id here

		setLocked(id, false)
		.then(function(){
			return res.json({status: 0});
		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		})

	});

	router.get('/:id/locked', function(req, res){
		var id = req.params.id;

		setLocked(id, true)
		.then(function(){
			return res.json({status: 0});
		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		})
	});

	router.get('/:id/unlocked', function(req, res){
		var id = req.params.id;

		setLocked(id, false)
		.then(function(){
			return res.json({status: 0});
		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		})

	});

	router.get('/:id/opened', function(req, res){
		var id = req.params.id;

		setClosed(id, false)
		.then(function(){
			return res.json({status: 0});

		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		});
	});

	router.get('/:id/closed', function(req, res){
		var id = req.params.id;

		setClosed(id, true)
		.then(function(){
			return res.json({status: 0});

		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		});
	});

	router.post('/add_device', function(req, res){
		console.log('add_device');
		var firebase_token = req.body.firebase_token;

		console.log("Firebase token: " + firebase_token);

		db.addDevice(firebase_token)
		.then(function(){
			return res.json({status: 0});
		})
		.catch(function(err){
			return res.json({status: 1, error: err});
		});


	});

	return router;
}


