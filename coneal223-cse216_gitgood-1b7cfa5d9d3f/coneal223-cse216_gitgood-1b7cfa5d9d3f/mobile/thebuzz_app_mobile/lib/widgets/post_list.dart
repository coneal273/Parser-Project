import 'package:flutter/material.dart';
import '../models/post.dart';
import '../utilities/posts.dart';
import 'comment_button.dart';
import 'vote_button.dart';

class PostList extends StatefulWidget {
  const PostList({super.key});

  @override
  State<PostList> createState() => _PostListState();
}

class _PostListState extends State<PostList> {
  List<Post>? ideas;
  var isLoaded = false;

  final _biggerFont = const TextStyle(fontSize: 18);
  @override
  void initState() {
    super.initState();
    sendRequest();
  }

  sendRequest() async {
    ideas = (await getIdeas());
    if (ideas != null) {
      setState(() => isLoaded = true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Visibility(
        visible: isLoaded,
        replacement: const Center(child: CircularProgressIndicator()),
        child: ListView.builder(
            itemCount: ideas?.length,
            shrinkWrap: true,
            itemBuilder: (context, index) {
              return Container(
                  padding: const EdgeInsets.all(16),
                  child: Expanded(
                    child: Column(
                      children: [
                        ListTile(
                          leading: Text(
                            ideas![index].votes.toString(),
                          ),
                          //inserts like button to right of ideas
                          title: Text(
                            ideas![index].content,
                          ),
                          //likes and comments per post
                          subtitle: commentButton(post_id: ideas![index].id),
                          trailing: LikeButton(post_id: ideas![index].id),
                        ),
                        ListView.builder(
                            itemCount: ideas?[index].comments.length,
                            shrinkWrap: true,
                            itemBuilder: (context, indexComment) {
                              return Container(
                                  padding: const EdgeInsets.all(16),
                                  child: Expanded(
                                    child: Column(
                                      children: [
                                        ListTile(
                                          //inserts like button to right of ideas
                                          title: Text(
                                            ideas![index]
                                                .comments[indexComment]
                                                .content
                                                .toString(),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ));
                            })
                      ],
                    ),
                  ));
            }));
  }
}
