const path = require("path");

const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");

module.exports = {
  entry: path.resolve(__dirname, "../", "src", "index.tsx"),
  mode: "production",
  target: "web",
  output: {
    //path: path.resolve(__dirname, "../build"),
    path: path.resolve(
      __dirname,
      "../",
      "../",
      "backend",
      "src",
      "main",
      "resources",
      "web"
    ),
    filename: "bundle.js",
  },

  module: {
    rules: [
      { test: /\.(ts|tsx)$/, loader: "ts-loader", exclude: /node_modules/ },
      { test: /\.css$/, use: ["style-loader", "css-loader"] },
      { test: /\.txt$/, use: "raw-loader" },
      {
        test: /\.m?js$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: [["@babel/preset-env", { targets: "defaults" }]],
          },
        },
      },
      // { test: /\.(js|jsx)$/ },
    ],
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, "..", "src", "index.html"),
    }),
    // new HtmlWebpackPlugin({
    //   template: path.resolve("..", "src", "components", "Login.js"),
    // }),
    new CopyWebpackPlugin({
      patterns: [
        { from: path.resolve(__dirname, "..", "public/"), to: "public/" },
      ],
    }),
  ],
  resolve: { extensions: [".ts", ".tsx", ".json", ".js", "jsx"] },
};
