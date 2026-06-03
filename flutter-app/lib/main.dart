import 'dart:math';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'home.dart';

void main() async {

  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  FirebaseAuth.instance
    .authStateChanges()
    .listen((User? user) {
      if(user == null){
        print('User is signed out');
      }else{
        print('User is signed in');
      }
  });

  runApp(ChangeNotifierProvider(
      create: (context) => LoginState(),
      child: const MaterialApp(
        debugShowCheckedModeBanner: false,
        home: LoginWidget()
      ),
  ));
}

class LoginWidget extends StatefulWidget {
  const LoginWidget({super.key});
  @override
  State<LoginWidget> createState() => _LoginWidgetState();
}

class _LoginWidgetState extends State<LoginWidget> {

  final _formKey = GlobalKey<FormState>();
  final TextEditingController _tecEmail = TextEditingController();
  final TextEditingController _tecPass = TextEditingController();

  bool _isLoading = false;
  bool _obscurePassword = true;

  @override
  void dispose() {
    _tecEmail.dispose();
    _tecPass.dispose();
    super.dispose();
  }

  Future<bool> signIn(String email, String pass) async {
    try{
      setState(() {
        _isLoading = true;
      });
      await FirebaseAuth.instance.signInWithEmailAndPassword(email: email, password: pass);
      return true;
    } on FirebaseAuthException catch (e) {
      String errorMessage = "Ocurrió un error al iniciar sesión.";

      if (e.code == 'user-not-found' || e.code == 'wrong-password' || e.code == 'invalid-credential') {
        errorMessage = 'Correo o contraseña incorrectos.';
      } else if (e.code == 'invalid-email') {
        errorMessage = 'El formato del correo no es válido.';
      } else if (e.code == 'network-request-failed') {
        errorMessage = 'Error de red. Revisa tu conexión.';
      }

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(errorMessage),
            backgroundColor: Colors.redAccent,
            behavior: SnackBarBehavior.floating,
          ),
        );
      }
      return false;
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context){
    var state = context.watch<LoginState>();

    return Scaffold(
      backgroundColor: Colors.grey[50],
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24.0),
        child: Form(
          key: _formKey,
          child:  Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              const Icon(
                Icons.lock_person_rounded,
                size: 80,
                color: Colors.blueAccent,
              ),
              const SizedBox(height: 16),

              const Text(
                "Ingresa tus credenciales para continuar",
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 14, color: Colors.grey),
              ),
              const SizedBox(height: 32),

              TextFormField(
                controller: _tecEmail,
                keyboardType: TextInputType.emailAddress,
                decoration: InputDecoration(
                  labelText: "Correo Electrónico",
                  prefixIcon: const Icon(Icons.email_outlined),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  filled: true,
                  fillColor: Colors.white,
                ),
                validator: (value) {
                  if (value == null || value.trim().isEmpty) {
                    return 'Por favor ingresa tu correo.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller: _tecPass,
                obscureText: _obscurePassword,
                decoration: InputDecoration(
                  labelText: "Contraseña",
                  prefixIcon: const Icon(Icons.lock_outlined),
                  suffixIcon: IconButton(
                    icon: Icon(
                      _obscurePassword ? Icons.visibility_off : Icons.visibility,
                    ),
                    onPressed: () {
                      setState(() {
                        _obscurePassword = !_obscurePassword;
                      });
                    },
                  ),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  filled: true,
                  fillColor: Colors.white,
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor ingresa tu contraseña.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24),

              _isLoading
              ? const Center(child: CircularProgressIndicator())
              : SizedBox(
                width: double.infinity,
                child:ElevatedButton(style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blueAccent,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 16.0),
                  elevation: 2,
                ),
                    onPressed: () async {
                      if(!_formKey.currentState!.validate()) return;

                      final email = _tecEmail.text.trim();
                      final password = _tecPass.text.trim();

                      bool success = await signIn(email, password);

                      if(success && mounted){
                        state.email = email;
                        state.password = password;

                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(
                              builder: (context) => ChangeNotifierProvider(
                                create: (context) => HomeState(),
                                child: const HomeWidget(),)
                          ),
                        );
                      }
                    }, child: const Text("Iniciar sesion", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),)
                ),
              )
            ],
          ),
        )
      ),
    );
  }
}

class LoginState extends ChangeNotifier{
  String _email = "";
  String _password = "";

  String get email => _email;
  String get password => _password;

  set email(String newValue){
    _email = newValue;
    notifyListeners();
  }
  set password(String newValue){
    _password = newValue;
    notifyListeners();
  }
}
