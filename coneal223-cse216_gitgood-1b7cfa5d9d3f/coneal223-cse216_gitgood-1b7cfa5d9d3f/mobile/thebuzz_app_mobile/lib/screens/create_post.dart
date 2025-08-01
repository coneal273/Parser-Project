import 'package:flutter/material.dart';
import '../widgets/post_form.dart';

class CreatePostScreen extends StatelessWidget {
  const CreatePostScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Create a new Post'),
        ),
        body: PostForm());
  }
}
