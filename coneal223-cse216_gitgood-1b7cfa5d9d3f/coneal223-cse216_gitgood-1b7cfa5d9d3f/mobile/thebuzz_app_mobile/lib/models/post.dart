import 'dart:convert';

//import 'comment.dart';

// List<PostModel> postModelFromJson(String str) =>
//     List<PostModel>.from(json.decode(str).map((x) => PostModel.fromJson(x)));

// String postModelToJson(List<PostModel> data) =>
//     json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

PostModel postModelFromJson(String str) => PostModel.fromJson(json.decode(str));

String postModelToJson(PostModel data) => json.encode(data.toJson());

class PostModel {
  PostModel({
    required this.status,
    required this.data,
  });

  String status;
  List<Post> data;

  factory PostModel.fromJson(Map<String, dynamic> json) => PostModel(
        status: json["status"],
        data: List<Post>.from(json["data"].map((x) => Post.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "status": status,
        "data": List<dynamic>.from(data.map((x) => x.toJson())),
      };
}

class Post {
  Post({
    required this.id,
    required this.user_id,
    required this.content,
    required this.votes,
    required this.comments,
    required this.active,
    required this.created,
  });

  int id;
  int user_id;
  String content;
  int votes;
  List<Comment> comments;
  bool active;
  String created; //CHANGE TO DATETIME AND PARSE

  factory Post.fromJson(Map<String, dynamic> json) => Post(
        id: json["id"],
        user_id: json["user_id"],
        content: json["content"],
        votes: json["votes"],
        comments: List<Comment>.from(
            json["comments"].map((x) => Comment.fromJson(x))),
        active: json["active"],
        created: json["created"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "user_id": user_id,
        "content": content,
        "votes": votes,
        "comments": List<dynamic>.from(comments.map((x) => x.toJson())),
        "active": active,
        "created": created,
      };
}

class Comment {
  Comment({
    required this.id,
    required this.user_id,
    required this.post_id,
    required this.content,
    required this.active,
    required this.created,
  });

  int id;
  int user_id;
  int post_id;
  String content;
  bool active;
  String created; //CHANGE TO DATETIME AND PARSE

  factory Comment.fromJson(Map<String, dynamic> json) => Comment(
        id: json["id"],
        user_id: json["user_id"],
        post_id: json["post_id"],
        content: json["content"],
        active: json["active"],
        created: json["created"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "user_id": user_id,
        "post_id": post_id,
        "content": content,
        "active": active,
        "created": created,
      };
}
