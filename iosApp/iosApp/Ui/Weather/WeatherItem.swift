//
//  WeatherItem.swift
//  iosApp
//
//  Created by openobject2 on 9/16/25.
//

import SwiftUI
import Shared

struct CurrentWeatherInfo: View {
    let state: WeatherState
    private let temp: String
    private let tempMax: String
    private let tempMin: String
    
    init(state: WeatherState) {
        self.state = state
        temp = "\(Int(state.curWeather!.main.temp.rounded()))℃"
        tempMax = "\(Int(state.curWeather!.main.tempMax.rounded()))℃"
        tempMin = "\(Int(state.curWeather!.main.tempMin.rounded()))℃"
    }
    
    var body: some View {
        VStack(alignment: .center, spacing: 16) {
            Text(state.coordinates?.first?.localName?.ko ?? state.curWeather!.name)
                .foregroundStyle(Color.white)
                .font(Font.system(size: 24))
            
            Text(temp)
                .foregroundStyle(Color.white)
                .fontWeight(.light)
                .font(.system(size: 48))
            
            Text(state.curWeather!.weather.first!.description_)
                .foregroundStyle(Color.white)
                .font(.system(size: 16))
            
            Text("최고 \(tempMax)\n최저 \(tempMin)")
                .foregroundStyle(Color.white)
                .font(.system(size: 14))
        }
        .fixedSize(horizontal: false, vertical: true)
    }
}

struct CitySearchDropdown: View {
    @State private var selectedCity = 0
    let isExpanded: Bool
    let availableCities: [CityCoordinate]
    let onCityClick: (Int) -> Void
    let onDismiss: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ForEach(availableCities.indices, id: \.self) { idx in
                Button(action: { onCityClick(idx) }) {
                    Text(availableCities[idx].localName?.ko ?? "")
                        .tag(idx)
                        .foregroundColor(.black)
                        .padding(.vertical, 12)
                        .padding(.horizontal, 16)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
        .fixedSize(horizontal: false, vertical: true)
        .background(Color.white)
        .cornerRadius(5)
        .shadow(radius: 5)
        .padding(.horizontal)
        .onDisappear(perform: onDismiss)
    }
}

struct OtherWeatherInfo: View {
    let otherItems: [OtherInfoUiModel]
    private let columns: [GridItem] = [
        GridItem(.flexible(), spacing: 4),
        GridItem(.flexible(), spacing: 4),
    ]
    
    var body: some View {
        LazyVGrid(columns: columns, spacing: 4) {
            ForEach(otherItems, id: \.title) { item in
                WeatherItem(title: item.title, content: item.content)
            }
        }
        .padding(.horizontal, 16)
        .fixedSize(horizontal: false, vertical: true)
    }
}

struct WeatherItem: View {
    let title: String
    let content: String
    
    var body: some View {
        VStack(alignment: .center) {
            Text(title).font(.footnote)
            Spacer(minLength: 10)
            getUnitAttributedText(prev: content, unitSize: 16)
        }
        .frame(maxWidth: .infinity)
        .foregroundStyle(.white)
        .padding(10)
        .background(
            RoundedRectangle(cornerRadius: 15).fill(Color(red: 0.52, green: 0.52, blue: 0.52, opacity: 0.52))
        )
    }
}
