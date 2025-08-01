import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:async';
import 'constants.dart';

import 'dart:developer' as developer;

/// post the likes to backend with IDToken and voted
void postVote(int post_id, bool liked) async {
  final response = await http.post(
    Uri.parse('$baseUrl/posts/$post_id/votes'),
    headers: {'Content-Type': 'application/json; charset=UTF-8'},
    body: jsonEncode({'user_id': 1, 'liked': liked}),
  );

  if (response.statusCode == 200) {
    developer.log(response.body);
  } else {
    throw Exception('Error code: ${response.statusCode}');
  }
}
