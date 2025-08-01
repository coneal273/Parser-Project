import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../utilities/auth.dart';

class viewProfile extends StatelessWidget {
  viewProfile({Key? key}) : super(key: key);
  AuthController authController = Get.find();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appbar will consist of back button, title, profile respectively
      appBar: AppBar(
        backgroundColor: Colors.yellow,
        leading: IconButton(
          icon: Icon(Icons.arrow_back_outlined),
          onPressed: () {
            // TODO: navigate BACK to the main page
            Navigator.pop(context);
          },
        ),
      ),
      //Right now only shows name and email through google, not editable
      body: Center(
        child: Wrap(
          spacing: 40,
          direction: Axis.vertical,
          children: const <Widget>[
            Text('Name: ', style: TextStyle(fontSize: 18)),
            Text('', //authController.googleAccount.value!.displayName ?? '',
                style: TextStyle(fontSize: 20)),
            Text('Email: ', style: TextStyle(fontSize: 18)),
            Text('', //authController.googleAccount.value?.email ?? '',
                style: TextStyle(fontSize: 20)),
            Text('Gender Identity: ', style: TextStyle(fontSize: 18)),
            Text('Sexual Orientation: ', style: TextStyle(fontSize: 18)),
            Text('Bio: ', style: TextStyle(fontSize: 18)),
          ],
        ),
      ),
    );
  }
}
