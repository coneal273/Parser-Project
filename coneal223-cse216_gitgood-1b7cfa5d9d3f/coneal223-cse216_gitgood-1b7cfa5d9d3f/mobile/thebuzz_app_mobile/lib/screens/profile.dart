import 'package:flutter/material.dart';
import '../widgets/profile.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(body: viewProfile());
  }
}
