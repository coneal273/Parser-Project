import 'package:flutter/material.dart';
import '../utilities/comments.dart';

class createComment extends StatefulWidget {
  const createComment({super.key, required this.post_id});

  final int post_id;
  @override
  State<createComment> createState() => _createCommentState();
}

class _createCommentState extends State<createComment> {
  final _commentController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Create a new Comment'),
      ),
      body: ListView(padding: const EdgeInsets.all(15.0), children: <Widget>[
        //creates space to enter title of idea
        TextFormField(
          controller: _commentController,
          decoration: const InputDecoration(
            hintText: 'Enter a Comment',
            border: OutlineInputBorder(),
          ),
          maxLength: 128,
          validator: (value) {
            if (value == null || value.isEmpty) {
              return "Please enter a Comment";
            }
            return null;
          },
        ),
        //creates space to enter content of idea

        Align(
            alignment: Alignment.center,
            //submit buttin for ideas
            child: MaterialButton(
              onPressed: () {
                postComment(widget.post_id, _commentController.text);
                _commentController.clear;
              },
              color: Colors.yellow[800],
              child:
                  const Text('Submit', style: TextStyle(color: Colors.black)),
            ))
      ]),
    );
  }
}
