import "./Card.css";

type Props = {
  children: React.ReactNode;
  className?: string;
};

export default function Card({ children, className = "" }: Props) {
  const classes = ["ui-card", className].filter(Boolean).join(" ");

  return <div className={classes}>{children}</div>;
}
