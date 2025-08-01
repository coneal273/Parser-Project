import 'dart:convert';

VoteModel voteModelFromJson(String str) => VoteModel.fromJson(json.decode(str));

String voteModelToJson(VoteModel data) => json.encode(data.toJson());

class VoteModel {
  VoteModel({
    required this.status,
    required this.data,
  });

  String status;
  List<Vote> data;

  factory VoteModel.fromJson(Map<String, dynamic> json) => VoteModel(
        status: json["status"],
        data: List<Vote>.from(json["data"].map((x) => Vote.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "status": status,
        "data": List<dynamic>.from(data.map((x) => x.toJson())),
      };
}

class Vote {
  Vote({
    required this.user_id,
    required this.post_id,
    required this.liked,
    required this.created,
  });

  int user_id;
  int post_id;
  bool liked;
  String created;

  factory Vote.fromJson(Map<String, dynamic> json) => Vote(
        user_id: json["user_id"],
        post_id: json["post_id"],
        liked: json["liked"],
        created: json["created"],
      );

  Map<String, dynamic> toJson() => {
        "user_id": user_id,
        "post_id": post_id,
        "liked": liked,
        "created": created,
      };
}
