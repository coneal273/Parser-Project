import 'dart:convert';

UserModel userModelFromJson(String str) => UserModel.fromJson(json.decode(str));

String userModelToJson(UserModel data) => json.encode(data.toJson());

class UserModel {
  UserModel({
    required this.id,
    required this.name,
    required this.username,
    required this.email,
    required this.gender_identity,
    required this.sexual_orientation,
    required this.note,
    required this.active,
    required this.created,
  });

  int id;
  String name;
  String username;
  String email;
  String gender_identity;
  String sexual_orientation;
  String note;
  bool active;
  DateTime created;

  factory UserModel.fromJson(Map<String, dynamic> json) => UserModel(
    id: json["id"],
    name: json["name"],
    username: json["username"],
    email: json["email"],
    gender_identity: json["gender_identity"],
    sexual_orientation: json["sexual_orientation"],
    note: json["note"],
    active: json["active"],
    created: json["created"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "name": name,
    "username": username,
    "email": email,
    "gender_identity": gender_identity,
    "sexual_orientation": sexual_orientation,
    "note": note,
    "active": active,
    "created": created,
  };
}