import 'package:flutter/material.dart';
import '../utilities/files.dart';

import 'dart:developer' as developer;

class FileButton extends StatelessWidget {
  FileButton({super.key});

  String b64File = '';

  @override
  Widget build(BuildContext context) {
    return MaterialButton(
      minWidth: 10,
      height: 10,
      onPressed: () async => {
        b64File = await chooseFile(),
        developer.log(b64File),
      },
      child: const Text('Upload file'),
    );
  }
}
