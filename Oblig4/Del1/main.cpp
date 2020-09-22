#include <iostream>
#include <fstream>
#include <string>
#include <list>
using namespace std;

static unsigned int A = 2654435769;

class HashMap {

private:
    list<string> *hashTable;
    int size;


private:
    unsigned long keyGen(string name) {
        unsigned long sum = 1;
        for(char i : name) {
            sum += sum * 7 + (i*7);
        }
        //cout << "Hash: " << sum;

        return multHash(sum, 7);
    }

    unsigned long multHash(unsigned sum, int x){
        const std::uint32_t knuth = 2654435769;
        return sum * knuth >> (32-x);
    }

public:
    //Inserts a new name in the list based on position returned from keygen
    void insertName(string name) {
        unsigned long pos = keyGen(name);
        if(hashTable[pos].size() > 0) {
            //Prints collision during insertion
            cout << "Collision on place " << pos << ". " << name << " will be put after " << hashTable[pos].back() << endl;
        }
        //cout << " | Pos: " << pos << "| Name: " << name << endl;
        hashTable[pos].push_back(name);
    }


    void listAll() {
        //Count number of collisions
        int count = 0;
        //Loops through all places on list
        for(int i = 0; i < size; i++) {
            cout << "Place " << i << ":";
            //Counts number of collisions
            if(hashTable[i].size() > 0) {
                count += hashTable[i].size()-1;
            }
            //Loops through if there are several names on same spot.
            for(string j : hashTable[i]) {
                cout << "--> " << j;
            }
            cout << endl;
        }
        cout << "Number of collisions: " << count << endl;
    }


    bool findName(string name) {
        unsigned long namePos = keyGen(name);
        for(string i : hashTable[namePos]) {
            if(name == i) {
                cout << name << " is located at " << namePos << " in the list" << endl;
                return true;
            }
        }
        cout << name << " does not exist in the list" << endl;
        return false;
    }


    //Constructor
    HashMap(int num) {
        size = num;
        hashTable = new list<string>[size];
    }
};


int main(){
    int desiredSize = 128;
    HashMap hash(desiredSize);

    ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig4/Del1/navn20.txt");
    string str;
    int count = 1;
    while (std::getline(file, str)) {
        hash.insertName(str);
        count++;
        //cout << str << "\n";
    }

    hash.listAll();

    hash.findName("Mads,Lundegaard");

    hash.findName("Knut");

    cout << "Lastfaktor: " << (double) count/desiredSize << endl;
}