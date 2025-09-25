//
//  WeatherScreen.swift
//  iosApp
//
//  Created by openobject2 on 9/15/25.
//

import SwiftUI
import Shared
import Lottie

struct WeatherScreen: View {
    @ObservedObject var viewModel: IosWeatherViewModel
    
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(
                    colors: [
                        Color(red: 43/255, green: 124/255, blue: 204/255),
                        Color(red: 124/255, green: 204/255, blue: 255/255)
                    ]
                ),
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()
            
            WeatherScreenBackground(
                forecast: viewModel.state?.threeHoursForecast?.list.first(
                    where: { $0.dt > Int64(Date().timeIntervalSince1970) }
                )
            )
            WeatherScreenForeground(viewModel: viewModel)
        }
    }
}

struct WeatherScreenForeground: View {
    @ObservedObject var viewModel: IosWeatherViewModel
    @State private var cityInput: String = ""
    
    init(viewModel: IosWeatherViewModel) {
        self.viewModel = viewModel
        self._cityInput = State(initialValue: viewModel.state?.searchQuery ?? "")
    }
    
    var body: some View {
        ScrollView {
            VStack {
                TextField(
                    "",
                    text: $cityInput,
                    prompt: Text("도시 이름을 검색하세요").foregroundColor(.white)
                )
                .onChange(of: cityInput) { viewModel.onSearchQueryChanged(query: cityInput) }
                .onSubmit { viewModel.getCoordinates() }
                .foregroundColor(.white)
                .padding()
                
                ZStack(alignment: .top) {
                    content
                    
                    if viewModel.state?.isDropdownExpanded == true,
                       let coord = viewModel.state?.coordinates,
                       !coord.isEmpty {
                        CitySearchDropdown(
                            isExpanded: viewModel.state?.isDropdownExpanded ?? false,
                            availableCities: viewModel.state?.coordinates ?? [],
                            onCityClick: { idx in viewModel.getCurWeather(coord: viewModel.state!.coordinates![idx]) },
                            onDismiss: { viewModel.onDropdownExpandedChanged(isExpanded: false) }
                        )
                    }
                }
            }
        }
        .scrollIndicators(.hidden)
    }
    
    @ViewBuilder
    private var content: some View {
        if viewModel.state == nil ||
            viewModel.state!.curWeather == nil ||
            !viewModel.state!.error.isEmpty
        {
            Text("검색 결과가 없습니다.")
                .foregroundStyle(.white)
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                .padding(.vertical, 56)
        } else {
            VStack(spacing: 20) {
                CurrentWeatherInfo(state: viewModel.state!)
                HourForecastContent(hourForecasts: viewModel.state!.hourForecasts)
                DayForecastContent(dayForecasts: viewModel.state!.dayForecasts)
                OtherWeatherInfo(otherItems: viewModel.state!.otherInfo)
                Spacer()
            }
            .background(Color.blue.opacity(0))
        }
    }
}

struct WeatherScreenBackground: View {
    let forecast: HourForecast?
    
    var body: some View {
        if let name = getForecastAnimationName(forecast: forecast) {
            ZStack {
                LottieView(animation: .named(name)).playing(loopMode: .loop)
                    .frame(width: 100, height: 100)
                    .padding(20)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
        }
    }
}
