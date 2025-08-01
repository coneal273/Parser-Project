import 'package:flutter/material.dart';
import 'comment_form.dart';

/// widget to create comment buttons
class commentButton extends StatefulWidget {
  const commentButton({super.key, required this.post_id});

  final int post_id;
  @override
  State<commentButton> createState() => commentButtonState();
}

class commentButtonState extends State<commentButton> {
  bool comment = false;

  @override
  Widget build(BuildContext context) {
    return Container(
        width: 70,
        child: Row(
          children: [
            MaterialButton(
                minWidth: 10,
                height: 10,
                color: comment ? Colors.red[800] : Colors.grey[300],
                onPressed: () => Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return createComment(post_id: widget.post_id);
                    })),
                child: const Text('Comment',
                    style: TextStyle(color: Colors.black))),
          ],
        ));
  }
}
