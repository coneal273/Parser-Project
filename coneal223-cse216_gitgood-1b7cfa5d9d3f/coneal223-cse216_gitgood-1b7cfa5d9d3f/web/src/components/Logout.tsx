import * as React from "react";
import { GoogleLogout } from "react-google-login";
import { useHistory } from "react-router-dom";

const clientId =
  "830605994462-rokgojp9krkqijhetf72mfm1r9gq0rqc.apps.googleusercontent.com";

function Logout() {
  let history = useHistory();

  const onSuccess = () => {
    alert("Logout made successfully");
    history.push("/login");
  };

  return (
    <div>
      <GoogleLogout
        clientId={clientId}
        buttonText="Logout"
        onLogoutSuccess={onSuccess}
      ></GoogleLogout>
    </div>
  );
}

export default Logout;
