import { GoogleLogin } from "react-google-login";
import { gapi } from "gapi-script";
import * as React from "react";
import { useHistory } from "react-router-dom";

const clientId =
  "830605994462-rokgojp9krkqijhetf72mfm1r9gq0rqc.apps.googleusercontent.com";
const URL = "http://localhost:4567";

function GoogleLoginPage() {
  let history = useHistory();

  //componentDidMount() {
  const initClient = () => {
    gapi.client.init({
      clientId: clientId,
      scope: "",
    });
  };
  gapi.load("client:auth2", initClient);
  //}

  const onSuccess = (res: any) => {
    console.log("success:", res);
    //history.push("/");
    //post to login route
    fetch(`${URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        idTokenString: res.tokenId, //tokenId? accessToken?
      }),
    })
      .then((response) => response.json())
      .then((response) => {
        sessionStorage.setItem("token", res.tokenId);
        localStorage.setItem("user_id", res.email);
        console.log("heyyy" + localStorage.getItem("email"));
        console.log(response);
        // sessionStorage.setItem('userid', response);
        history.push("/");
      })
      .catch((error) => console.error(error));
  };

  const onFailure = (err) => {
    console.log("failed:", err);
  };

  //   render() {
  return (
    <div>
      <div>The Buzz</div>
      <GoogleLogin
        clientId={clientId}
        buttonText="Sign in with Google"
        onSuccess={onSuccess}
        onFailure={onFailure}
        cookiePolicy={"single_host_origin"}
        isSignedIn={true}
      />
      {/* <> {Authenticated()}</> */}
    </div>
  );
  //}
}

export default GoogleLoginPage;
