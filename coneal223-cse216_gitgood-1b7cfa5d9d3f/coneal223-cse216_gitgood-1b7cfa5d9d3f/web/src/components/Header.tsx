import * as React from "react";
import { GoogleLogout } from "react-google-login";
import Authenticated from "./Authenticated";
import { NavBar } from "./NavBar";
// const {Container, Navbar, Nav} = require('react-bootstrap');
// const { PersonCircle } = require('react-bootstrap-icons');

function Header(props: any) {
  Authenticated();

  return (
    <>
      <NavBar />
    </>
  );
}

export default Header;
