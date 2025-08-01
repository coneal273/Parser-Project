import 'dart:convert';

List<CommentModel> commentModelFromJson(String str) => List<CommentModel>.from(
    json.decode(str).map((x) => CommentModel.fromJson(x)));

String commentModelToJson(List<CommentModel> data) =>
    json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class CommentModel {
  CommentModel({
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
  DateTime created;

  factory CommentModel.fromJson(Map<String, dynamic> json) => CommentModel(
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

// https://www.digitalocean.com/community/tutorials/flutter-flutter-http
// structure of Post is from copying the raw JSON into this website: https://app.quicktype.io/

// Two helper functions to convert a string to a List<Post>
// List<Comment> commentFromJson(String str) {
//   //string has the value associated with a comments key for a single post
//   var comments = json.decode(
//       str); //potentially move if statement to here to check comments instead of str
//   if (comments == null) {
//     //bailing on the comment if it is null
//     print("Comment is null");
//     return List<Comment>.empty();
//   }
//   print("Res from comments(commentFromJson): " + comments.toString());
//   return List<Comment>.from(comments.map((x) => Comment.fromJson(x)));
// }

// // and a List<Post> to String
// String postToJson(List<Comment> data) =>
//     json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

// class Comment {
//   Comment({
//     // The different variables of the JSON
//     required this.id,
//     required this.user_id,
//     required this.post_id,
//     required this.content,
//     required this.created,
//   });
//   int id;
//   int user_id;
//   int post_id;
//   String content;
//   String created;

//   factory Comment.fromJson(json) => Comment(
//         id: json["id"],
//         user_id: json["user_id"],
//         post_id: json["post_id"],
//         content: json["content"],
//         created: json["created"],
//       );

//   Map<String, dynamic> toJson() => {
//         "id": id,
//         "user_id": user_id,
//         "post_id": post_id,
//         "content": content,
//         "created": created,
//       };
// }