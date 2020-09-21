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
    unsigned int keyGen(string name) {
        unsigned long sum = 1;
        for(char i : name) {
            sum += sum * 7 + (i*7);
        }
        return hashGen(sum);
    }


    //Generate hash
    unsigned int hashGen(unsigned long sum) {
        return sum * A >> (32 - 7);
    }

public:

    void insertName(string name) {
        int hash = keyGen(name);
        cout << hash << "\n";
    }



    //Constructor
    HashMap(int num) {
        size = num;
        hashTable = new list<string>[size];
    }
};


int main(){

    HashMap hash(128);
    hash.insertName("caro");

    ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig4/Del1/navn20.txt");
    string str;
    int count = 1;
    while (std::getline(file, str)) {
        count++;
        cout << str << "\n";
    }
    cout << count;
}