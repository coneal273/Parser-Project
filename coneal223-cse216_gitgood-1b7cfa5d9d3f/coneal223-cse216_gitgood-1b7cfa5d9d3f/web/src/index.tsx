import * as React from "react";
import * as ReactDOM from "react-dom";
import { Auth0Provider } from "@auth0/auth0-react";

import { App } from "./App";
import "./style.css";

ReactDOM.render(
  <React.StrictMode>
    <App num={21} />
  </React.StrictMode>,
  document.getElementById("app")
);
