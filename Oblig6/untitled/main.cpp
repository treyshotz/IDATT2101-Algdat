#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <queue>

using namespace std;
typedef pair<int, int> intPair;


class Graph {

public:
    int edges;
    int nodes;
    vector<intPair> *adjList2;

    explicit Graph(int nodes, int edges) {
        this->nodes = nodes;
        this->edges = edges;
        adjList2 = new vector<intPair>(edges);
    }

    void addEdge(vector<pair<int, int> > adjacency_list[], int node1, int node2, int weight) {
        adjacency_list[node1].emplace_back(node2, weight);

        //adjacency_list[node2].emplace_back(node1, weight);
    }

    void shortestPath(vector<pair<int, int>> adjacency_list[], int start) {

        priority_queue<intPair, vector<intPair>, greater<intPair> > heap;

        vector<int> distance(nodes, INT_MAX);

        heap.push(make_pair(0, start));
        distance[start] = 0;

        while (!heap.empty()) {
            int a = heap.top().second;
            heap.pop();

            for (auto x : adjacency_list[a]) {
                int b = x.first;
                int weight = x.second;

                if (distance[b] > distance[a] + weight) {
                    distance[b] = distance[a] + weight;
                    heap.push(make_pair(distance[b], b));
                }
            }
        }
        cout << "Shortest paths from startpoint " << start << ":" << endl;
        cout << "Node\t \t Distance" << endl;
        for (int i = 0; i < nodes; i++) {
            if (distance[i] == INT_MAX) {
                cout << i << "\t \t \t " <<"Nåes ikke" << endl;
            } else {
                cout << i << "\t \t \t " << distance[i] << endl;
            }
        }
    }

};

int main() {
    //Sets starting point on the graph
    int start = 0;

    //Sets up graph
    int nodes;
    int edges;
    string str;
    ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig6/untitled/vgSkandinavia.txt");
    getline(file, str);
    istringstream iss(str);
    iss >> nodes;
    iss >> edges;

    //Creating the list which will contain all the edges
    Graph graph(nodes, edges);
    vector<intPair> adjList[nodes+1];

    int node1;
    int node2;
    int weight;
    for (int i = 0; i < edges; i++) {
        getline(file, str);
        istringstream iss2(str);
        iss2 >> node1;
        iss2 >> node2;
        iss2 >> weight;
        graph.addEdge(adjList, node1, node2, weight);
    }

    graph.shortestPath(adjList, start);
    return 0;
}