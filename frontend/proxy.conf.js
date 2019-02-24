const PROXY_CONFIG = [
  {
    context: [
      "/api",
      "/socket"
    ],
    target: "http://localhost:8090",
    secure: false
  }
]

module.exports = PROXY_CONFIG;
