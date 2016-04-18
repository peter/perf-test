var express = require('express');
var app = express();
var request = require('request');

app.get('/', function (req, res) {
  res.send('Node.js/Express app');
});

app.get('/1', function (req, res) {
  request('http://localhost:9000/articles.json', function (error, response, body) {
    if (!error) {
      try {
        res.json(JSON.parse(body));
      } catch (e) {
        res.status(500).send('Could not parse JSON: ' + e);
      }
    } else {
      res.status(500).send('Could not fetch JSON: ' + error);
    }
  });
});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});
