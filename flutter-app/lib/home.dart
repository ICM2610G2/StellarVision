import 'dart:io';

import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:geocoding/geocoding.dart';
import 'package:image_picker/image_picker.dart';

class HomeWidget extends StatefulWidget {
  const HomeWidget({super.key});
  @override
  State<HomeWidget> createState() => _HomeWidgetState();
}

class _HomeWidgetState extends State<HomeWidget>{

  final _formKey = GlobalKey<FormState>();

  final db = FirebaseFirestore.instance;
  final FirebaseStorage _storage = FirebaseStorage.instance;

  final TextEditingController _tecName = TextEditingController();
  final TextEditingController _tecAdress = TextEditingController();

  File? _selectedImage;

  @override
  void dispose() {
    _tecName.dispose();
    _tecAdress.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final ImagePicker picker = ImagePicker();
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);

    if (image != null) {
      setState(() {
        _selectedImage = File(image.path);
      });
    }
  }

  @override
  Widget build(BuildContext context){
    var state = context.watch<HomeState>();

    return Scaffold(
      backgroundColor: Colors.grey[50],
      appBar: AppBar(
        title: const Text("Registrar Lugar", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24.0),
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [

              const Icon(
                Icons.location_on,
                size: 80,
                color: Colors.blueAccent,
              ),
              const SizedBox(height: 16),

              const Text(
                  "Ingrese los datos del lugar que desea registrar",
                  textAlign: TextAlign.center,
                  style: TextStyle(fontSize: 14, color: Colors.grey)),
              const SizedBox(height: 32),

              TextFormField(
                  controller: _tecName,
                  keyboardType: TextInputType.text,
                  decoration: InputDecoration(
                    labelText: "Nombre del lugar",
                    prefixIcon: const Icon(Icons.drive_file_rename_outline),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    filled: true,
                    fillColor: Colors.white
                  ),
                  validator: (value) {
                    if (value == null || value.trim().isEmpty) {
                      return 'Por favor ingresa el nombre del lugar.';
                    }
                    return null;
                  },
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller: _tecName,
                keyboardType: TextInputType.text,
                decoration: InputDecoration(
                    labelText: "Dirección del lugar",
                    prefixIcon: const Icon(Icons.location_city),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    filled: true,
                    fillColor: Colors.white
                ),
                validator: (value) {
                  if (value == null || value.trim().isEmpty) {
                    return 'Por favor ingresa el nombre del lugar.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              if(_selectedImage != null)
                Container(
                    height: 150,
                    margin: const EdgeInsets.all(10),
                    child: Image.file(_selectedImage!, fit: BoxFit.cover)
                ),

              SizedBox(
                width: double.infinity,
                child: ElevatedButton(style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blueAccent,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 16.0),
                  elevation: 2,
                ),
                    onPressed: _pickImage,
                    child: const Text("Seleccionar imagen")
                ),
              ),

              const SizedBox(height: 12),

              SizedBox(
                width: double.infinity,
                child: ElevatedButton(style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.deepPurpleAccent,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 16.0),
                  elevation: 2,
                ),
                    onPressed: () async {
                      String name = _tecName.text.trim();
                      String address = _tecAdress.text.trim();

                      if(name.isEmpty || address.isEmpty){
                        ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text("Por favor, complete todos los campos"))
                        );
                        return;
                      }

                      if(_selectedImage == null){
                        ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text("Por favor, seleccione una imagen..."))
                        );
                        return;
                      }

                      try{
                        state.title = name;
                        state.address = address;

                        List<Location> locations = await locationFromAddress(address);

                        if(locations.isNotEmpty){
                          Location location = locations.first;
                          GeoPoint geoPosition = GeoPoint(location.latitude, location.longitude);

                          final fileName = '${name.toLowerCase().replaceAll(' ', '_')}_spot.png';
                          final String storagePath = 'places/$fileName';

                          final storageRef = _storage.ref().child(storagePath);
                          await storageRef.putFile(_selectedImage!);

                          await db.collection("places").add(
                              {
                                "title": name,
                                "position": geoPosition,
                                "imagePath": storagePath,
                              }
                          );
                          if(mounted){
                            ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text("Lugar registrado exitosamente"))
                            );
                            _tecName.clear();
                            _tecAdress.clear();
                            setState(() {
                              _selectedImage = null;
                            });
                          }
                        } else {
                          if(mounted){
                            ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text("No se pudo encontrar la ubicación"))
                            );
                          }
                        }
                      } catch (e) {
                        if(mounted){
                          ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text("Error al registrar el lugar: $e"))
                          );
                        }
                      } finally {
                        state.title = "";
                        state.address = "";
                      }
                    },
                    child: const Text("Registrar Lugar")
                ),
              ),
            ],
          )
        )
      ),
    );
  }
}

class HomeState extends ChangeNotifier{
  String _title = "";
  String _address = "";


  String get title => _title;
  String get address => _address;

  set title(String newValue){
    _title = newValue;
    notifyListeners();
  }
  set address(String newValue){
    _address = newValue;
    notifyListeners();
  }
}