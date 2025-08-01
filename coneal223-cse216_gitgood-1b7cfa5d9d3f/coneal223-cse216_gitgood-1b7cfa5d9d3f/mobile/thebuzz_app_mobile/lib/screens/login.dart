import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../utilities/auth.dart';

class LoginScreen extends StatelessWidget {
  LoginScreen({Key? key}) : super(key: key);
  //final controller = Get.put(LoginContoller());
  AuthController authController = Get.find();

  @override
  Widget build(BuildContext context) {
    const appTitle = 'The Buzz';
    return Scaffold(
        body: Center(
            child: FloatingActionButton.extended(
      onPressed: () {
        authController.login();
      },
      icon: Icon(Icons.security),
      label: Text('Sign in with Google'),
      backgroundColor: Colors.white,
      foregroundColor: Colors.black,
    )));
  }
}
