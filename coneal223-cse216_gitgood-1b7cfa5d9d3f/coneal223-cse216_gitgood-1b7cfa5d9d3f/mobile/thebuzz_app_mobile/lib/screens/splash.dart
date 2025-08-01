import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:thebuzz_app_mobile/utilities/auth.dart';
import '../screens/home.dart';
import '../screens/login.dart';

class SplashScreen extends StatelessWidget {
  //const SplashScreen({Key? key}) : super(key: key);

  final AuthController _authController = Get.put(AuthController());

  // Future<void> init() async {
  //   _authController.checkLogin();
  // }

  // @override
  // Widget build(BuildContext context) {
  //   return FutureBuilder(
  //     future: init(),
  //     builder: (context, snapshot) {

  //     }
  //   )
  // }

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      if (_authController.isLoggedIn.value) {
        return HomeScreen();
      } else {
        return HomeScreen(); //CHANGE THIS WHEN OAUTH IS FIXED
        //return LoginScreen();
      }
    });
  }
}
