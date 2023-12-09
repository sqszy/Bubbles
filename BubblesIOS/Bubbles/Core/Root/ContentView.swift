//
//  ContentView.swift
//  Bubbles
//
//  Created by Петр Михеев on 19.09.2023.
//

import SwiftUI


struct ContentView: View {
    @StateObject var authViewModel = AuthViewModel()
    var body: some View {
        Group {
            if authViewModel.isAuth {
                HomeView().environmentObject(authViewModel)
            } else {
                LoginView().environmentObject(authViewModel)
            }
        }
    }

}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
