import * as React from "react";
import { useHistory } from "react-router-dom";

function Authenticated() {
  let history = useHistory();

  console.log(sessionStorage.getItem("token") + "Heyy");

  if (sessionStorage.getItem("token") === null) history.push("/login");
  //history.push("/");
  return;
}

export default Authenticated;
