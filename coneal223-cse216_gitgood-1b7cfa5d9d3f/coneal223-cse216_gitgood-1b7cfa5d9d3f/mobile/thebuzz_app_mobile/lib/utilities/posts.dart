import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:async';
import '../models/post.dart';
import 'constants.dart';

//split out logging into its own class?
import 'dart:developer' as developer;

/// method to get ideas from database
/// @return list of ideas from the database
Future<List<Post>> getIdeas() async {
  final response = await http.get(Uri.parse('$baseUrl/posts'));

  if (response.statusCode == 200) {
    //return PostModel.fromJson(json.decode(response.body));
    //return postModelFromJson(response.body);
    //move logging
    developer.log(response.body);
    // var res = jsonDecode(response.body);
    // //move logging
    // print('json decode: $res');
    // var posts = res['data'];
    // return posts;
    //return postModelFromJson(ideas);
    PostModel res = postModelFromJson(response.body);
    return res.data;
  } else {
    throw Exception('Did not recieve success status code from request.');
  }
}

/// Method to post an idea to the database
void postIdea(String content) async {
  final response = await http.post(
    Uri.parse('$baseUrl/posts'),
    headers: {'Content-Type': 'application/json; charset=UTF-8'},
    body: jsonEncode({
      'user_id': 1,
      'content': content,
    }),
  );

  if (response.statusCode == 200) {
    developer.log(response.body);
  } else {
    throw Exception('Error code: ${response.statusCode}');
  }
}
