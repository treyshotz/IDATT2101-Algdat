#include <iostream>
#include <fstream>
#include <string>
#include <list>
using namespace std;


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
        return (unsigned)sum * (unsigned)2654435769 >> (32-7);
    }


public:
    //Inserts a new name in the list based on position returned from keygen
    void insertName(string name) {
        unsigned long pos = keyGen(name);
        if(hashTable[pos].size() > 0) {
            //Prints collision during insertion
            cout << "Collision on place " << pos << ". " << name << " will be put after " << hashTable[pos].back() << endl;
        }
        hashTable[pos].push_back(name);
    }

    //Prints all names in the list and returns number of collisions
    int listAll() {
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
        return count;
    }


    //Print if a name is found or not
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
    explicit HashMap(int num) {
        size = num;
        hashTable = new list<string>[size];
    }
};


int main(){
    int desiredSize = 128;
    HashMap hash(desiredSize);

    //Use this if compiling with g++
    ifstream file("navn20.txt");

    //Use this if compiling with Clion
    //ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig4/Del1/navn20.txt");


    string str;
    int count = 1;
    while (std::getline(file, str)) {
        hash.insertName(str);
        count++;
    }

    int collisions = hash.listAll();
    cout << "Number of collisions: " << collisions << endl;

    hash.findName("Mads,Lundegaard");

    hash.findName("Knut");

    cout << "Lastfaktor: " << (double) count/desiredSize << endl;

    cout << "Collisions per name is: " << (double) collisions/count << endl;
}