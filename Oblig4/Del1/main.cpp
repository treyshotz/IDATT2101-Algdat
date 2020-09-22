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
        unsigned long sum = 0;
        for(char i : name) {
            sum += sum * 7 + (i*7);
        }
        return sum % size;
        //return sum * A >> (32 - 7);
    }


public:
    void insertName(string name) {
        unsigned long hash = keyGen(name);
        hashTable[hash].push_back(name);
    }


    void listAll() {
        //Loops through all places on list
        for(int i = 0; i < size; i++) {
            cout << "Place " << i << ":";
            //Loops through if there are several names on same spot.
            for(string j : hashTable[i]) {
                cout << "--> " << j;
            }
            cout << endl;
        }
    }

    //Constructor
    HashMap(int num) {
        size = num;
        hashTable = new list<string>[size];
    }
};


int main(){

    HashMap hash(128);

    ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig4/Del1/navn20.txt");
    string str;
    int count = 1;
    while (std::getline(file, str)) {
        hash.insertName(str);
        count++;
        //cout << str << "\n";
    }

    hash.listAll();

    cout << count;
}