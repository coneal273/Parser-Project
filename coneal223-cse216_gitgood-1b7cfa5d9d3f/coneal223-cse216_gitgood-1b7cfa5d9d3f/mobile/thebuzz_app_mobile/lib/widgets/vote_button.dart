import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../utilities/votes.dart';

class LikeButton extends StatefulWidget {
  const LikeButton({super.key, required this.post_id});

  final int post_id;
  @override
  State<LikeButton> createState() => _LikeButtonState();
}

class _LikeButtonState extends State<LikeButton> {
  //add check for already pressed by user?
  bool liked = false;
  bool disliked = false;

//widget to build the like button and post values if pressed
  @override
  Widget build(BuildContext context) {
    return Container(
        width: 70,
        child: Row(
          children: [
            MaterialButton(
                //like button
                minWidth: 10,
                height: 10,
                color: liked ? Colors.green[800] : Colors.grey[300],
                child:
                    const Text('Like', style: TextStyle(color: Colors.black)),
                onPressed: () => {
                      postVote(widget.post_id, true),
                      if (disliked) {disliked = false},
                      liked = !liked,
                    }),
            MaterialButton(
                //dislike button
                minWidth: 10,
                height: 10,
                color: disliked ? Colors.red[800] : Colors.grey[300],
                child: const Text('Dislike',
                    style: TextStyle(color: Colors.black)),
                onPressed: () => {
                      postVote(widget.post_id, false),
                      if (liked) {liked = false},
                      disliked = !disliked,
                    }),
          ],
        ));
  }
}
