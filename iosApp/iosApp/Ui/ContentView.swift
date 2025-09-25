import SwiftUI
import Shared

enum Destination: String, CaseIterable, Identifiable {
    case weather = "날씨"
    case todo = "할일"
    
    var id: String { self.rawValue }
    var label: String { return self.rawValue }
}

struct ContentView: View {
    @State private var selectedTab: Destination = .weather
    @State var showContent = false
    @State private var showCategoryDialog = false
    @StateObject private var todoViewModel = IosTodoViewModel()
    @StateObject private var weatherViewModel = IosWeatherViewModel()
    
    var body: some View {
        VStack {
            Picker("Tabs", selection: $selectedTab) {
                ForEach(Destination.allCases) { destination in
                    Text(destination.label).tag(destination)
                }
            }
            .backgroundStyle(.red)
            .padding(.horizontal)
            .pickerStyle(.segmented)
            
            switch selectedTab {
            case .weather:
                WeatherScreen(viewModel: weatherViewModel)
            case .todo:
                TodoScreen(viewModel: todoViewModel)
            }
        }
    }
}

struct ContentItem<Content: View, Destination: View>: View {
    let title: String
    let content: Content
    let destination: Destination
    
    init(title: String, @ViewBuilder content: () -> Content, @ViewBuilder destination: () -> Destination) {
        self.title = title
        self.content = content()
        self.destination = destination()
    }
    
    var body: some View {
        VStack {
            HStack(alignment: .center) {
                Text(title).font(.headline)
                Spacer()
                NavigationLink(
                    destination: { destination },
                    label: { Text("더보기").font(.caption).foregroundStyle(.gray) }
                )
            }
            .padding([.horizontal, .top])
            
            content
        }
        .frame(maxHeight: .infinity, alignment: .topLeading)
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
