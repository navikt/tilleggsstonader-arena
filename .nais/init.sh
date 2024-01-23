echo "- exporting database username and password -"
export DB_USERNAME=$(cat "$DB_USER_PATH" 2> /dev/null || echo $DB_USERNAME)
export DB_PASSWORD=$(cat "$DB_PASSWORD_PATH" 2> /dev/null || echo $DB_PASSWORD)
echo "- exported DB_USERNAME og DB_PASSWORD for tilleggsstonader-arena "

if [ -z "$DB_USERNAME" ]
then
  echo "username har ikke verdi"
fi

if [ -z "$DB_PASSWORD" ]
then
  echo "password har ikke verdi"
fi