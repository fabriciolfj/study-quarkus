# shellcheck disable=SC1068
count = 0
while (( count++ <= 10000 )); do
   curl -i -H "Content-Type: application/json" -X GET -d "2.03" http://192.168.49.2:31692/transactions/87878787/balance
   echo
done