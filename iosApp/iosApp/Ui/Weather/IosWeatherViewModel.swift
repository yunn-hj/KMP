//
//  IosWeatherViewModel.swift
//  iosApp
//
//  Created by openobject2 on 9/15/25.
//

import Foundation
import Shared
import Combine
import Dispatch
import KMPNativeCoroutinesCombine

class IosWeatherViewModel: ObservableObject {
    private let weatherRepo = Repositories.shared.weatherRepository
    private let weatherViewModel = ViewModels.shared.weatherViewModel
    
    @Published var state: WeatherState? = nil
    
    private var cancellables = Set<AnyCancellable>()
    
    init() {
        createPublisher(for: weatherViewModel.state)
            .receive(on: DispatchQueue.main)
            .sink { completion in
                if case .failure(let error) = completion {
                    dump("yhj_weatherViewModel >> stateFlow subscription failed: \(error)")
                }
            } receiveValue: { [weak self] weatherState in
                self?.state = weatherState
                dump("yhj_weatherViewModel >> \(weatherState)")
            }
            .store(in: &cancellables)
    }
    
    func onSearchQueryChanged(query: String) {
        weatherViewModel.onSearchQueryChanged(query: query)
    }
    
    func onDropdownExpandedChanged(isExpanded: Bool) {
        weatherViewModel.onDropdownExpandedChanged(expanded: isExpanded)
    }
    
    func getCoordinates() {
        weatherViewModel.getCoordinates()
    }
    
    func getCurWeather(coord: CityCoordinate?) {
        if coord != nil {
            weatherViewModel.getCurWeather(coord: coord!)
        }
    }
}
