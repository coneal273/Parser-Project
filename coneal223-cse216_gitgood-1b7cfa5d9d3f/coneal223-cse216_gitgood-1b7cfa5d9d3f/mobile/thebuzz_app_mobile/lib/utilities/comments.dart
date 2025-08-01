import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:async';
import 'constants.dart';

import 'dart:developer' as developer;

/// Method to post an idea to the database
/// @param {string} title - title of the post
/// @param {string} content - content of the idea
void postComment(int post_id, String content) async {
  final response = await http.post(
    Uri.parse('$baseUrl/posts/$post_id/comments'),
    headers: {'Content-Type': 'application/json; charset=UTF-8'},
    body: jsonEncode({'user_id': 1, 'content': content}),
  );

  if (response.statusCode == 200) {
    developer.log(response.body);
  } else {
    throw Exception('Error code: ${response.statusCode}');
  }
}
