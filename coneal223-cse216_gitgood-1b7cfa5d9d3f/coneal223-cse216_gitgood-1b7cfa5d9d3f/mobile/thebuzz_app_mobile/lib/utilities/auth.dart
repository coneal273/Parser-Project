import 'package:get/get.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'constants.dart';

///controller for handling google oauth2 authentication
class AuthController extends GetxController {
  ///boolean for user login status
  final isLoggedIn = false.obs;
  GoogleSignInAccount? googleAccount;

  final GoogleSignIn _googleSignin = GoogleSignIn(scopes: [
    'email',
    'https://www.googleapis.com/auth/contacts.readonly',
    //'https://www.googleapis.com/auth/userinfo.profile'
  ]);
  //var googleAccount = Rx<GoogleSignInAccount?>(null);

  ///async function to log in user
  void login() async {
    //await _googleSignin.signIn().then((resp) => googleAccount.value = resp);
    // GoogleSignInAccount? googleAccount;
    //GoogleSignInAuthentication gAuth;

    await _googleSignin.currentUser?.clearAuthCache();

    GoogleSignInAccount? googleAccount = await _googleSignin.signIn();
    GoogleSignInAuthentication gAuth = await googleAccount!.authentication;

    var response = await http.post(
      Uri.parse('$baseUrl/login'),
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'idTokenString': gAuth.idToken,
      }),
    );
    print(response.body);

    if (response.statusCode == 200) {
      isLoggedIn.value = true;
    } else {
      throw Exception('Error logging in');
    }
  }

  ///function to log out user
  void logout() {
    isLoggedIn.value = false;
  }

  ///function to check if user is already logged in
  void checkLogin() {
    final token = isLoggedIn.value;
    if (isLoggedIn.value) {
      isLoggedIn.value = true;
    }
  }
}
