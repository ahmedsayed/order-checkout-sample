
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER sample WITH PASSWORD 'sample';
    CREATE DATABASE sample;
    GRANT ALL PRIVILEGES ON DATABASE sample TO sample;
EOSQL
