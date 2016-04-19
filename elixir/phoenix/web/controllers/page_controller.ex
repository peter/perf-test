defmodule HelloPhoenix.PageController do
  use HelloPhoenix.Web, :controller

  def index(conn, _params) do
    render conn, "index.html"
  end

  def get(conn, _params) do
    #data = Poison.decode!(HTTPotion.get("http://localhost:9000/articles.json", [timeout: 10_000]).body)
    data = Poison.decode!(HTTPoison.get!("http://localhost:9000/articles.json").body)
    json conn, data
  end
end
