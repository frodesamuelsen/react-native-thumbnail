require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name           = package['name']
  s.version        = package['version']
  s.summary        = package['description']
  s.description    = package['description']
  s.license        = package['license']
  s.author         = package['author']
  s.homepage       = "https://github.com/yiky84119/react-native-thumbnail"
  s.source         = { :git => 'https://github.com/yiky84119/react-native-thumbnail.git' }

  s.requires_arc   = true
  s.platform       = :ios, '9.0'

  s.source_files   = 'ios/**/*.{h,m}'

  s.dependency 'React'
end
