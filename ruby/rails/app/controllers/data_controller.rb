class DataController < ApplicationController
  def get
    data = JSON.parse(Excon.get('http://localhost:9000/articles.json').body)
    render json: data
  end
end
