#include <iostream>
using namespace std;

enum class Note{ Do,Re, Mi, Fa, Sol, La, Si};
inline ostream& operator <<(ostream& out , Note n){
  switch(n){
  case Note::Do: out << "Do"; break;
  case Note::Re: out << "Ré"; break;
  case Note::Mi: out << "Mi"; break;
  case Note::Fa: out << "Fa"; break;
  case Note::Sol: out << "Sol"; break;
  case Note::La: out << "La"; break;
  case Note::Si: out << "Si"; break;
  }
  return out;
}
//classe Alternative
enum class Alteration {diese, bemol, rien};
inline ostream& operator <<( ostream& out, Alteration a){
  switch(a){
  case Alteration::diese: out << "diese"; break;
  case Alteration::bemol: out << "bemol"; break;
  case Alteration::rien: out << "rien"; break;
  }
  return out;
}

// classe Qualificatif
enum class Qualificatif { mineur, majeur};

inline ostream& operator <<( ostream& out,Qualificatif a){
  switch(a){
  case Qualificatif::mineur: out << "mineur"; break;
  case Qualificatif::majeur: out << "majeur"; break;
  }
  return out;
}

//classe Tonalite
class Tonalite{
private:
  Note nom;
  Alteration alteration;
  Qualificatif qualif;
public:
  Tonalite(Note n, Alteration a, Qualificatif q);
  void affiche();
};
Tonalite::Tonalite(Note n, Alteration a, Qualificaf q){
  nom = n;
  alteration = a;
  qualif = q;
}
void Tonalite::affiche(){
  cout "Tonalite: de nom" << nom << "alter: " << alteration << "qualif: " << qualif << endl;
}

int main(){
  Note n {Note::Re};
  Alteration a {Alteration::rien};
  Qualificatif q {Qualificatif::mineur};
  cout << q <<endl;
  cout << a << endl;
  cout << n << endl;
  Tonalite t{Note::Fa,Alteration::rien,Qualificatif::mineur};
  t.affiche();
  
  
	     }
    
  
