import pandas as pd
from jobspy import scrape_jobs
import logging
from urllib.parse import unquote

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def search_jobs(search_term):
    # Decode double-encoded terms for safety
    decoded_term = unquote(unquote(search_term))
    logger.info(f"Searching jobs for term: '{decoded_term}'")

    try:
        jobs = scrape_jobs(
            site_name=["indeed","linkedin","google"],
            search_term=decoded_term,
            google_search_term=f"{decoded_term} jobs in Morocco since yesterday",
            location="Morocco",
            results_wanted=3,
            hours_old=72,
            country_indeed='Morocco'
        )
    except Exception as e:
        logger.error(f"Scraping error for term '{decoded_term}': {e}")
        jobs = []

    # Log number of jobs found
    logger.info(f"Found {len(jobs)} jobs for term '{decoded_term}'")

    df = pd.DataFrame(jobs)
    df = df.drop(columns=["job_level","job_function","emails"], errors="ignore")

    # Ensure salary-related columns exist
    for col in ["min_amount", "max_amount", "currency", "interval"]:
        if col not in df.columns:
            df[col] = None

    df["salaryRange"] = df.apply(
        lambda row: (
            f"{int(row.min_amount)} - {int(row.max_amount)} {row.currency}/{row.interval}"
            if pd.notnull(row.min_amount) and pd.notnull(row.max_amount) else None
        ),
        axis=1
    )
    df = df.drop(columns=["min_amount","max_amount","currency"], errors="ignore")

    df = df.rename(columns={
        "site":"source",
        "skills":"requiredSkills",
        "job_url":"applyUrl",
        "company":"companyName",
        "job_type":"jobType",
        "date_posted":"postedDate",
        "company_url":"companyUrl",
        "company_url_direct":"companyUrlDirect",
        "company_addresses":"companyAddresses",
        "company_num_employees":"companyNumEmployees",
        "company_revenue":"companyRevenue",
        "company_description":"companyDescription",
        "experience_range":"experienceRange",
        "company_industry":"companyIndustry",
        "job_url_direct":"jobUrlDirect",
        "is_remote":"isRemote"
    })

    columns_to_keep = ["source","requiredSkills","applyUrl","title","companyName","location",
                       "jobType","description","salaryRange","postedDate","companyUrl",
                       "companyUrlDirect","companyAddresses","companyNumEmployees",
                       "companyRevenue","companyDescription","experienceRange",
                       "companyIndustry","jobUrlDirect","isRemote"]

    for col in columns_to_keep:
        if col not in df.columns:
            df[col] = None

    df = df[columns_to_keep]
    df = df.where(pd.notnull(df), None)

    # Log the first few jobs for extra confirmation
    if len(df) > 0:
        logger.info(f"Sample job titles: {df['title'].head(3).tolist()}")
    
    return df.to_dict(orient="records")
