import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../screens/home.dart';
import '../utilities/posts.dart';
import 'file_button.dart';

/// widget to create page to allow users to post new ideas
class PostForm extends StatefulWidget {
  const PostForm({super.key});

  @override
  State<PostForm> createState() => _PostFormState();
}

class _PostFormState extends State<PostForm> {
  final _contentController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return ListView(padding: const EdgeInsets.all(15.0), children: <Widget>[
      //creates space to enter content of idea
      TextFormField(
        controller: _contentController,
        decoration: const InputDecoration(
          hintText: 'Post content',
          border: OutlineInputBorder(),
        ),
        maxLength: 500,
        validator: (value) {
          if (value == null || value.isEmpty) {
            return "Please enter post content";
          }
          return null;
        },
      ),
      FileButton(), //button for uploading files
      Align(
          alignment: Alignment.center,
          //submit buttin for ideas
          child: MaterialButton(
            onPressed: () {
              String content = _contentController.text;
              postIdea(content);
              _contentController.clear;
              //Get.to(() => HomeScreen());
              Get.back();
            },
            color: Colors.yellow[800],
            child: const Text('Submit', style: TextStyle(color: Colors.black)),
          ))
    ]);
  }
}
