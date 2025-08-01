import 'dart:convert';
import 'dart:io';
import 'package:file_picker/file_picker.dart';

/// Function to pick a file from mobile storage
Future<String> chooseFile() async {
  FilePickerResult? result = await FilePicker.platform.pickFiles();

  if (result != null) {
    //var fileBytes = result.files.first.path.toString();
    File file = File(result.files.first.path.toString());
    String b64 = base64Encode(file.readAsBytesSync());
    return b64;
    //String b64 = base64Encode(fileBytes);
    //return b64;
  }
  return ''; //file selection was canceled
}
