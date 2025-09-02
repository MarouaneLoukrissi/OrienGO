from flask import Flask, request, jsonify
import services.Recommender as generate_recommendations
from services import job_scraper


# ========== FLASK API ==========
app = Flask(__name__)

@app.route("/recommendations", methods=["POST"])
def recommendations():
    data = request.get_json()
    if not data or "riasec" not in data:
        return jsonify({"error": "Vous devez fournir un objet JSON avec 'riasec'"}), 400
    
    riasec_result = data["riasec"]
    output = generate_recommendations.generate_recommendations(riasec_result)
    return jsonify(output)

@app.route("/jobs/search", methods=["GET"])
def search_jobs_route():
    search_term = request.args.get("search_term")
    if not search_term:
        return jsonify({"error": "Vous devez fournir un 'search_term'"}), 400
    try:
        output = job_scraper.search_jobs(search_term)  # now unambiguous
        return jsonify(output)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True, port=5000)