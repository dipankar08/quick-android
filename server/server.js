var WebSocketServer = require('ws').Server,
  server = new WebSocketServer({port: 40510})

server.on('connection', function (client) {
  client.on('message', function (message) {
    console.log('received: %s', message)
  })
  client.on('data', function (message) {
    console.log('received: %s', message)
  })
  setInterval(
    () => client.send(`${new Date()}`),
    1000
  )
})
