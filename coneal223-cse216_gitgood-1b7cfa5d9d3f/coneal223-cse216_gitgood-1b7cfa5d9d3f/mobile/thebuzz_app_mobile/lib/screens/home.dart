import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:thebuzz_app_mobile/screens/profile.dart';
import '../utilities/auth.dart';
import '../widgets/post_list.dart';
import 'create_post.dart';

class HomeScreen extends StatelessWidget {
  HomeScreen({super.key});

  AuthController authController = Get.find();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('The Buzz'),
        // in action widget we have PopupMenuButton
        actions: [
          PopupMenuButton<int>(
            itemBuilder: (context) => [
              // PopupMenuItem 1
              PopupMenuItem(
                value: 1,
                // row with 2 children
                child: Row(
                  children: const [
                    Icon(Icons.star),
                    SizedBox(
                      width: 10,
                    ),
                    Text("Create Idea")
                  ],
                ),
              ),
              // PopupMenuItem 2
              PopupMenuItem(
                value: 2,
                // row with two children
                child: Row(
                  children: const [
                    Icon(Icons.chrome_reader_mode),
                    SizedBox(
                      width: 10,
                    ),
                    Text("Sign Out")
                  ],
                ),
              ),
              PopupMenuItem(
                value: 3,
                // row with two children
                child: Row(
                  children: const [
                    Icon(Icons.heart_broken_outlined),
                    SizedBox(
                      width: 10,
                    ),
                    Text("Ideas")
                  ],
                ),
              ),
              PopupMenuItem(
                value: 4,
                // row with two children
                child: Row(
                  children: const [
                    Icon(Icons.on_device_training_outlined),
                    SizedBox(
                      width: 10,
                    ),
                    Text("Profile")
                  ],
                ),
              ),
            ],
            offset: const Offset(0, 100),
            color: Colors.yellow,
            elevation: 2,
            // on selected we show the dialog box
            onSelected: (value) {
              // if value 1 show dialog
              if (value == 1) {
                //create post screen
                Get.to(() => CreatePostScreen());
                // if value 2 show dialog
              } else if (value == 2) {
                //signs user out
                authController.logout();
              } else if (value == 3) {
                //home screen (all ideas)
                Get.to(() => HomeScreen());
                // if value 2 show dialog
              } else if (value == 4) {
                //user profile page
                Get.to(() => ProfileScreen());
              }
            },
          ),
        ],
      ),
      body: const PostList(),
    );
  }
}
